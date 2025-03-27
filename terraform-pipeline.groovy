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
                sh 'terraform init'
            }
        }

        stage('Terraform Plan') {
            steps {
                sh 'terraform plan -out=tfplan'
            }
        }

        stage('Terraform Apply') {
            when {
                branch 'main' // Apply only on the main branch
            }
            steps {
                sh 'terraform apply -auto-approve tfplan'
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
