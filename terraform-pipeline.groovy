pipeline {
    agent any  // Runs on any available Jenkins agent


    stages {
        stage('Checkout Code') {
            steps {
                script{
                    try {
                        sh 'git clone \'https://github.com/nikitha-git05/soure-file.git\' /var/repo/soure-file'  // Replace with your repo
                    }
                    catch (Exception e) {
                        sh 'whoami'
                        sh 'cd /var/repo/soure-file;git config --global --add safe.directory /var/repo/soure-file;git pull' 
                    }
                }
            }
        }

        stage('Terraform Init') {
            steps {
                sh 'cd /var/repo/soure-file;terraform init'
            }
        }

        stage('Terraform validate') {
            steps {
                sh 'cd /var/repo/soure-file;terraform validate'
            }
        }

        stage('Terraform Plan') {
            steps {
                sh 'cd /var/repo/soure-file;terraform plan'
            }
        }

        stage('Terraform Apply') {
            when {
                branch 'main' // Apply only on the main branch
            }
            steps {
                sh 'cd /var/repo/soure-file;terraform apply'
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
