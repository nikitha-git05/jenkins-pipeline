pipeline {
    agent any  // Runs on any available Jenkins agent


    stages {
        stage('Checkout Code') {
            steps {
                sh 'git clone \'https://github.com/nikitha-git05/soure-file.git\' /var/soure-file'  // Replace with your repo
            }
        }

        stage('Terraform Init') {
            steps {
                sh 'cd /var/soure-file;terraform init'
            }
        }

        stage('Terraform validate') {
            steps {
                sh 'cd /var/soure-file;terraform validate'
            }
        }

        stage('Terraform Plan') {
            steps {
                sh 'cd /var/soure-file;terraform plan'
            }
        }

        stage('Terraform Apply') {
            when {
                branch 'main' // Apply only on the main branch
            }
            steps {
                sh 'cd /var/soure-file;terraform apply'
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
