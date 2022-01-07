node("${NODE}") {
    withCredentials([usernamePassword(
        credentialsId: "${DOCKER_CRED_ID}",
        usernameVariable: 'USERNAME', 
        passwordVariable: 'PASSWORD'
    )]) {
        stage('git branch main') {
            git branch: 'main', url: 'https://github.com/vskurikhin/DayBook-3.0.git'
        }
        stage('gradle clean build') {
            try {
                sh '''
                  set +x
                  gradle clean build \
                        -Dquarkus.package.type=native \
                        -Dquarkus.native.remote-container-build=true
                '''
            } catch (err) {
                echo "Error message: " + err.getMessage()
            }
            echo "currentBuild.result: ${currentBuild.result}"
        }
        stage("docker build $USERNAME/daybook3-build-${BUILD_NUMBER}") {
            try {
                sh '''
                    set +x
                    docker build \
                        -f src/main/docker/Dockerfile.native \
                        -t ${USERNAME}/daybook3-build-${BUILD_NUMBER} \
                        .
                '''
            } catch (err) {
                echo "Error message: " + err.getMessage()
            }
            echo "currentBuild.result: ${currentBuild.result}"
        }
        stage('docker login') {
            sh '''
              set +x
              docker login --username ${USERNAME} --password $PASSWORD
            '''
        }
        stage("docker push $USERNAME/daybook3-build-${BUILD_NUMBER}") {
            try {
                sh '''
                    set +x
                    docker push ${USERNAME}/daybook3-build-${BUILD_NUMBER}
                '''
            } catch (err) {
                echo "Error message: " + err.getMessage()
            }
            echo "currentBuild.result: ${currentBuild.result}"
        }
        stage('sleep') {
            for (int i = 0; i < 10; i++) {
                sh '$i - sleep 1'
            }
        }
    }
}
