/*
@param TEAM_NAME, TEAM_PROFILE, EMAIL, SLACK_CHANNEL, SONAR_PROJECT will be declared in Jenkins job.
		env.JOB_NAME : Show Jenkins job name.
		env.BUILD_NUMBER: Show current Jenkins build number.
		env.BUILD_URL: Show Jenkins job URL.
*/

def FAILED_STAGE

def SONAR_MODULE = 'employee-core,employee-ui'

def qg

@NonCPS // has to be NonCPS or the build breaks on the call to .each
def for_loop(DEPLOY_FILES) {
    DEPLOY_FILES.each { FILE ->
       sh "mvn wildfly:deploy-only -Dwildfly.hostname=${WILDFLY_IP} -Dwildfly.port=${WILDFLY_PORT} -Dwildfly.username=${WILDFLY_USERNAME} -Dwildfly.password=${WILDFLY_PASSWORD} -Dwildfly.deployment.filename=${FILE}"
    }
}
/*
	This Pipelines are made up of multiple steps. It is include Checkout, Build, SonarQube, Check Quality Gate and Deploy application to server.
	It also send a notification by email or slack to who has permission to build the job.
*/
pipeline {
	agent any
   	tools {
    	maven 'MAVEN'
  	}
  	stages {
	    stage('Checkout'){
      	   	steps {	
				script{
					FAILED_STAGE = 'Checkout'
					checkout scm 
				}
			}
      	}
      	stage('Build employee-core'){
	        steps {
				script{
					FAILED_STAGE = 'Build'
					sh 'mvn clean install -Pemployee-core'  
				}
         	}
	    }
	    stage('Build employee-ui'){
	        steps {
				script{
					FAILED_STAGE = 'Build'
					sh 'mvn clean install -Pemployee-ui'  
				}
         	}
	    }
		stage('SonarQube'){
	  		environment {
        		scannerHome = tool 'SonarQubeScanner'
   			}
			steps{
				script{
					FAILED_STAGE = 'SonarQube'
					withSonarQubeEnv('SonarQube') {
						sh "${scannerHome}/bin/sonar-scanner -Dsonar.host.url=http://192.168.70.59:8889 -Dsonar.language=java -Dsonar.projectName=${SONAR_PROJECT} -Dsonar.projectKey=${SONAR_PROJECT} -Dsonar.tests=unittest -Dsonar.sources=src/main/java -Dsonar.java.libraries=/var/jenkins_home/.m2/repository/org/projectlombok/lombok/1.18.2/lombok-1.18.2.jar -Dsonar.modules=${SONAR_MODULE} -Dsonar.java.binaries=. -Dsonar.java.coveragePlugin=jacoco "
						}
					}
				}
			}
		stage('Quality Gate'){
			steps{
				script{
					sleep(10)
					FAILED_STAGE = 'Quality Gate'
					timeout(time:15, unit:'MINUTES'){
						qg = waitForQualityGate()
						if (qg.status != 'OK'){
							currentBuild.result = 'FAILURE'
							error "Pipeline aborted due to quality gate failure: ${qg.status}"
						}
					}
				}
			}
		}
		stage('Deploy'){
	        steps {
				script{
					FAILED_STAGE = 'Deploy' 
					for_loop(${DEPLOY_FILES})
				}
			}
		}
	}
	
}
