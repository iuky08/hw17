def allureTestOpsProjectId = '164'

pipeline {
    agent any
    tools {
        gradle "Gradle 6.8.3"
    }
    parameters {
        string(name: 'SELENOID_URL', defaultValue: 'selenoid.autotests.cloud')
        choice(name: 'VIDEO_ENABLED',
                choices: ['true', 'false'])
        credentials(name: 'REMOTE_WEB_DRIVER_CRED_ID',
                description: 'Username and password for remote web driver',
                defaultValue: '1933267a-4824-4f65-9bfe-d8a47445ee39',
                credentialType: "usernamePassword",
                required: true)
        string(name: 'THREADS', defaultValue: '2')
    }
    stages {
        stage('Test') {
            steps {
                withAllureUpload(name: '${JOB_NAME} - #${BUILD_NUMBER}', projectId: allureTestOpsProjectId, results: [[path: 'build/allure-results']], serverId: 'allure-server', tags: 'tags') {
                    withCredentials([
                            usernamePassword(credentialsId: '${REMOTE_WEB_DRIVER_CRED_ID}', usernameVariable: 'REMOTE_WEB_DRIVER_USERNAME', passwordVariable: 'REMOTE_WEB_DRIVER_PASSWORD')
                    ]) {
                        sh 'gradle clean test' +
                                ' -Dthreads="${THREADS}"' +
                                ' -Dweb.remote.driver.url="${SELENOID_URL}"' +
                                ' -Dvideo.enabled="${VIDEO_ENABLED}"' +
                                ' -Dweb.remote.driver.user="${REMOTE_WEB_DRIVER_USERNAME}"' +
                                ' -Dweb.remote.driver.password="${REMOTE_WEB_DRIVER_PASSWORD}"'
                    }
                }
            }
        }
    }

    post {
        always {
                sh "java" +
                        " -Dbuild.launch.name='${JOB_NAME} - #${BUILD_NUMBER}'" +
                        " -Dbuild.env='ENV'" +
                        " -Dbuild.report.link='${BUILD_URL}'" +
                        " -Dproject.name='${JOB_BASE_NAME}'" +
                        " -Dlang='ru'" +
                        " -Denable.chart='true'" +
                        " -Dallure.report.folder='./allure-report/'" +
                        " -jar ${allureNotificationsFile}"
            }
        }
    }
}