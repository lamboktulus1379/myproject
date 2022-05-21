pipeline {
  agent any
  environment {
    ARTIFACT_REGISTRY_REPOSITRY = "asia-southeast2-docker.pkg.dev/sonic-charmer-337916/bhinneka"
    LOCATION = "asia-southeast2-docker.pkg.dev"
    PROJECT_ID = "sonic-charmer-337916"
    IMAGE_TAG = "0.0.1"
    IMAGE_NAME = "bhinneka"
  }
  stages {
    stage('Maven Install') {
      agent {
        docker {
          image 'maven:3.8.1'
        }
      }
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }

    stage("Build and Push Image to Artifact Registry") {
        steps {
            script {
                withDockerRegistry(credentialsId: 'gcr:sonic-charmer-337916', url: "https://${env.LOCATION}") {
                    app = docker.build("${env.ARTIFACT_REGISTRY_REPOSITRY}/${env.IMAGE_NAME}")
                    app.push("${env.IMAGE_TAG}")
                }
            }
        }
    }
  }
}