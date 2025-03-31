    pipeline {
    agent any  // Runs on any available Jenkins agent

    environment {
        REPO_URL = 'https://github.com/nikitha-git05/soure-file.git'
        REPO_DIR = '/var/repo/soure-file'
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    if (fileExists(REPO_DIR)) {
                        sh "cd ${REPO_DIR} && git pull"
                    } else {
                        sh "git clone ${REPO_URL} ${REPO_DIR}"
                    }
                }
            }
        }

        stage('INIT AWS') {
            steps {
                dir(REPO_DIR) {
                    sh 'aws configure set aws_access_key_id "AKIAXZ5NF342O2ERRYML" && aws configure set aws_secret_access_key "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" && aws configure set region "us-east-2" && aws configure set output "json"'
                }
            }
        }
        
        stage('Terraform Init') {
            steps {
                dir(REPO_DIR) {
                    sh 'terraform init'
                }
            }
        }

        stage('Terraform Validate') {
            steps {
                dir(REPO_DIR) {
                    sh 'terraform validate'
                }
            }
        }

        stage('Terraform Plan') {
            steps {
                dir(REPO_DIR) {
                    sh 'terraform plan -out aws.tfstate'
                }
            }
        }

        stage('Terraform Apply') {
            when {
                branch 'main'  // Apply only on the main branch
            }
            steps {
                dir(REPO_DIR) {
                    sh 'terraform apply -auto-approve'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/*.tfstate', fingerprint: true
        }
        failure {
            echo 'Terraform execution failed! Check logs for details.'
        }
    }
}
