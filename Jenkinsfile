pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'JDK 21'
    }

    environment {
        DOCKER_IMAGE = 'alysongustavo/eventhub-management-service'
        IMAGE_TAG = "v1.0.${env.BUILD_NUMBER}"

        AWS_REGION = 'us-east-1'
        EKS_CLUSTER_NAME = 'eventhub-management-cluster'

        DOCKER_CREDS = credentials('docker-hub-credentials-id')
        AWS_CREDS = credentials('aws-credentials-id')
    }

    stages {

        stage('1. Build (Skip Tests)') {
            steps {
                echo 'Building source code and generating the .jar file without running tests...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('2. SonarQube Analysis') {
            steps {
                echo 'Running static code analysis with SonarQube...'
                withSonarQubeEnv('SonarQube-Server') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('3. Unit & Integration Tests') {
            steps {
                echo 'Running unit and integration tests...'
                sh 'mvn verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml, target/failsafe-reports/*.xml'
                }
            }
        }

        stage('4. Build & Push Docker Image') {
            steps {
                echo "Building Docker image ${DOCKER_IMAGE}:${IMAGE_TAG}..."
                sh "docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} -t ${DOCKER_IMAGE}:latest ."

                echo 'Logging in and pushing image to Docker Hub...'
                sh "echo \$DOCKER_CREDS_PSW | docker login -u \$DOCKER_CREDS_USR --password-stdin"

                sh "docker push ${DOCKER_IMAGE}:${IMAGE_TAG}"
                sh "docker push ${DOCKER_IMAGE}:latest"
            }
            post {
                always {
                    sh "docker logout"
                    sh "docker rmi ${DOCKER_IMAGE}:${IMAGE_TAG} || true"
                    sh "docker rmi ${DOCKER_IMAGE}:latest || true"
                }
            }
        }

        stage('5. Deploy to AWS EKS') {
            steps {
                script {
                    def environment = "dev"

                    sh "aws eks update-kubeconfig --region ${AWS_REGION} --name ${EKS_CLUSTER_NAME}"

                    sh "kubectl apply -k k8s/overlays/${environment}"
                }
            }
        }
    }

    post {
        success {
            echo "🚀 Pipeline completed successfully! Version ${IMAGE_TAG} deployed to EKS."
        }
        failure {
            echo "❌ Pipeline failed. Please check the logs."
        }
    }
}