pipeline {
    agent any

    tools {
        jdk 'JDK17'
    }

    environment {
        APP_NAME = "s3-service-demo"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh '''
                    chmod +x mvnw
                    ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                withCredentials([string(
                        credentialsId: 'dockerhub-username',
                        variable: 'DOCKERHUB_USERNAME')])
                {
                    sh '''
                        docker build \
                        -t ${DOCKERHUB_USERNAME}/${APP_NAME}:latest .
                    '''
                }
            }
        }

        stage('Login and Push Docker Image') {
            steps {
                withCredentials([
                        string(
                        credentialsId: 'dockerhub-username',
                        variable: 'DOCKERHUB_USERNAME'
                        ),
                        string(
                            credentialsId: 'dockerhub-token',
                            variable: 'DOCKERHUB_TOKEN'
                        )])
                {
                    sh '''
                        echo "$DOCKERHUB_TOKEN" | docker login \
                            -u "$DOCKERHUB_USERNAME" \
                            --password-stdin

                        docker push \
                            ${DOCKERHUB_USERNAME}/${APP_NAME}:latest

                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'dockerhub-username',
                        variable: 'DOCKERHUB_USERNAME'
                    ),
                    string(
                        credentialsId: 'ec2-host',
                        variable: 'EC2_HOST'
                    ),
                    string(
                        credentialsId: 'ec2-user',
                        variable: 'EC2_USER'
                    ),
                    string(
                        credentialsId: 'rds-endpoint',
                        variable: 'RDS_ENDPOINT'
                    ),
                    string(
                        credentialsId: 'rds-database',
                        variable: 'RDS_DATABASE'
                    ),
                    string(
                        credentialsId: 'rds-username',
                        variable: 'RDS_USERNAME'
                    ),
                    string(
                        credentialsId: 'rds-password',
                        variable: 'RDS_PASSWORD'
                    )
                ]) {

                    sshagent(['ec2-ssh-key']) {

                        sh '''
                            ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} "
                                echo '===== Pulling latest image ====='
                                docker pull ${DOCKERHUB_USERNAME}/${APP_NAME}:latest

                                echo '===== Removing old container ====='
                                docker rm -f ${APP_NAME} || true

                                echo '===== Starting container ====='
                                docker run -d \\
                                    --name ${APP_NAME} \\
                                    -p 5555:5555 \\
                                    -e RDS_ENDPOINT='${RDS_ENDPOINT}' \\
                                    -e RDS_DATABASE='${RDS_DATABASE}' \\
                                    -e RDS_USERNAME='${RDS_USERNAME}' \\
                                    -e RDS_PASSWORD='${RDS_PASSWORD}' \\
                                    ${DOCKERHUB_USERNAME}/${APP_NAME}:latest

                                echo '===== Container Status ====='
                                docker ps
                            "
                        '''
                    }
                }
            }
        }
    }

    post {

        success {
            echo '===== BUILD AND DEPLOYMENT SUCCESSFUL ====='
        }

        failure {
            echo '===== PIPELINE FAILED ====='
        }
    }
}
