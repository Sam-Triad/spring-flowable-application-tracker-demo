#!/bin/bash

set -e

echo "Waiting for Keycloak to be ready..."
until curl -sf http://keycloak:18080/auth/realms/camunda-platform > /dev/null 2>&1; do
  echo "Keycloak not ready yet, retrying in 5 seconds..."
  sleep 5
done

echo "Keycloak is ready. Configuring groups, users, and client roles..."

# Get admin token
echo "Obtaining admin token..."
TOKEN_RESPONSE=$(curl -s -X POST "http://keycloak:18080/auth/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli")

ADMIN_TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)

if [ -z "$ADMIN_TOKEN" ]; then
  echo "Failed to obtain admin token"
  echo "Response: $TOKEN_RESPONSE"
  exit 1
fi

echo "Admin token obtained successfully"

# Create Verifier group
echo "Creating Verifier group..."
curl -s -X POST "http://keycloak:18080/auth/admin/realms/camunda-platform/groups" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Verifier"}'

# Create First_Level_Admin group
echo "Creating First_Level_Admin group..."
curl -s -X POST "http://keycloak:18080/auth/admin/realms/camunda-platform/groups" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "First_Level_Admin"}'

# Create Applicants group
echo "Creating Applicant group..."
curl -s -X POST "http://keycloak:18080/auth/admin/realms/camunda-platform/groups" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Applicant"}'

# Get group IDs
echo "Retrieving group IDs..."
GROUPS_RESPONSE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/groups" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

VERIFIER_GROUP_ID=$(echo $GROUPS_RESPONSE | grep -o '"id":"[^"]*","name":"Verifier"' | grep -o '"id":"[^"]*' | cut -d'"' -f4)
First_Level_Admin_GROUP_ID=$(echo $GROUPS_RESPONSE | grep -o '"id":"[^"]*","name":"First_Level_Admin"' | grep -o '"id":"[^"]*' | cut -d'"' -f4)
Applicant_GROUP_ID=$(echo $GROUPS_RESPONSE | grep -o '"id":"[^"]*","name":"Applicant"' | grep -o '"id":"[^"]*' | cut -d'"' -f4)

echo "Verifier Group ID: $VERIFIER_GROUP_ID"
echo "First_Level_Admin Group ID: $First_Level_Admin_GROUP_ID"
echo "Applicant Group ID: $Applicant_GROUP_ID"

# Get user IDs
echo "Retrieving user IDs..."
VERIFIER_USER_RESPONSE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/users?username=verifier" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

FIRST_LEVEL_ADMIN_USER_RESPONSE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/users?username=first_level_admin" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

APPLICANT_USER_RESPONSE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/users?username=test_applicant" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

VERIFIER_USER_ID=$(echo $VERIFIER_USER_RESPONSE | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
FIRST_LEVEL_ADMIN_USER_ID=$(echo $FIRST_LEVEL_ADMIN_USER_RESPONSE | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
APPLICANT_USER_RESPONSE_ID=$(echo $APPLICANT_USER_RESPONSE | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)

echo "Verifier User ID: $VERIFIER_USER_ID"
echo "First Level Admin User ID: $FIRST_LEVEL_ADMIN_USER_ID"
echo "First Level Admin User ID: $APPLICANT_USER_RESPONSE_ID"

# Assign verifier user to Verifier group
echo "Assigning verifier user to Verifier group..."
curl -s -X PUT "http://keycloak:18080/auth/admin/realms/camunda-platform/users/$VERIFIER_USER_ID/groups/$VERIFIER_GROUP_ID" \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# Assign first_level_admin user to First_Level_Admin group
echo "Assigning first_level_admin user to First_Level_Admin group..."
curl -s -X PUT "http://keycloak:18080/auth/admin/realms/camunda-platform/users/$FIRST_LEVEL_ADMIN_USER_ID/groups/$First_Level_Admin_GROUP_ID" \
  -H "Authorization: Bearer $ADMIN_TOKEN"

# Assign first_level_admin user to Applicant group
echo "Assigning test_applicant user to Applicant group..."
curl -s -X PUT "http://keycloak:18080/auth/admin/realms/camunda-platform/users/$APPLICANT_USER_RESPONSE_ID/groups/$Applicant_GROUP_ID" \
  -H "Authorization: Bearer $ADMIN_TOKEN"

echo "Users assigned to groups successfully!"

# Configure springboot-api client as admin client
echo "Configuring springboot-api client for admin operations..."

# Get all clients
CLIENTS_RESPONSE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/clients" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

# Extract springboot-api client details
SPRINGBOOT_SECTION=$(echo $CLIENTS_RESPONSE | grep -o '"clientId":"springboot-api"[^}]*"serviceAccountUserId":"[^"]*' || echo "")
SPRINGBOOT_CLIENT_ID=$(echo $CLIENTS_RESPONSE | grep -o '"id":"[^"]*","clientId":"springboot-api"' | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
SERVICE_ACCOUNT_ID=$(echo $SPRINGBOOT_SECTION | grep -o '"serviceAccountUserId":"[^"]*' | cut -d'"' -f4)

echo "Springboot API Client ID: $SPRINGBOOT_CLIENT_ID"
echo "Service Account User ID: $SERVICE_ACCOUNT_ID"

# Get realm-management client ID
REALM_MGMT_CLIENT_ID=$(echo $CLIENTS_RESPONSE | grep -o '"id":"[^"]*","clientId":"realm-management"' | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)

echo "Realm Management Client ID: $REALM_MGMT_CLIENT_ID"

# Get required roles from realm-management client
echo "Fetching realm-management roles..."
MANAGE_USERS_ROLE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/clients/$REALM_MGMT_CLIENT_ID/roles/manage-users" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

VIEW_USERS_ROLE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/clients/$REALM_MGMT_CLIENT_ID/roles/view-users" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

QUERY_USERS_ROLE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/clients/$REALM_MGMT_CLIENT_ID/roles/query-users" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

QUERY_GROUPS_ROLE=$(curl -s -X GET "http://keycloak:18080/auth/admin/realms/camunda-platform/clients/$REALM_MGMT_CLIENT_ID/roles/query-groups" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

# Assign roles to springboot-api service account
echo "Assigning admin roles to springboot-api service account..."
ASSIGN_RESPONSE=$(curl -s -X POST "http://keycloak:18080/auth/admin/realms/camunda-platform/users/$SERVICE_ACCOUNT_ID/role-mappings/clients/$REALM_MGMT_CLIENT_ID" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d "[$MANAGE_USERS_ROLE, $VIEW_USERS_ROLE, $QUERY_USERS_ROLE, $QUERY_GROUPS_ROLE]")

echo "Response: $ASSIGN_RESPONSE"
echo "Springboot-api client configured with admin permissions successfully!"
echo "Setup complete!"