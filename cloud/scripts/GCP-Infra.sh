#!/bin/bash

# Set your GCP project ID and other variables
SERVICE_ACCOUNT_KEY="path of service-account-key"
PROJECT_ID="Project-Id"
CLUSTER_NAME="cluster-gcp-deployment"
CLUSTER_REGION="us-central1-c"
REGION="us-central1"
TOPIC_NAME="Vehicle"
DB_REGION="us-east1"
DB_TYPE="firestore-native"
ARTIFACTORY_NAME="gcp-demo-app"
ARTIFACTORY_FORMAT="docker"

# Authenticate with Google Cloud using application default credentials
gcloud auth activate-service-account --key-file=$SERVICE_ACCOUNT_KEY

# Set new Project Id
gcloud config set project $PROJECT_ID

# Enable required APIs
gcloud services enable container.googleapis.com pubsub.googleapis.com cloudfunctions.googleapis.com firestore.googleapis.com cloudbuild.googleapis.com eventarc.googleapis.com run.googleapis.com artifactregistry.googleapis.com

# Create Github Container Registry
gcloud artifacts repositories create $ARTIFACTORY_NAME --repository-format=$ARTIFACTORY_FORMAT --location=$REGION

# Create a GKE cluster
gcloud container clusters create $CLUSTER_NAME --num-nodes=2 --region=$CLUSTER_REGION --scopes "https://www.googleapis.com/auth/cloud-platform"

# Get Cluster Credentials
gcloud container clusters get-credentials $CLUSTER_NAME --zone $CLUSTER_REGION --project $PROJECT_ID

# Create a Pub/Sub topic
gcloud pubsub topics create $TOPIC_NAME --project=$PROJECT_ID

#Create Firestore DB
gcloud alpha firestore databases create --location=$DB_REGION --type=$DB_TYPE

echo "Infrastructure and services set up successfully."
