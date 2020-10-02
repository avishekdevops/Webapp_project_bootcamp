node
{
try
{
    def dockerHome = tool name: 'docker-1', type: 'dockerTool'
    def dockerCMD = "${dockerHome}/bin/docker"
    def mavenHome = tool name: 'maven-3', type:'maven'        
    def mavenCMD = "${mavenHome}/bin/mvn"
    stage('Git checkout')
    {        
        git credentialsId: 'gitscred', url: 'https://github.com/sourav-ch-tcs/javawebapp.git' 
    }      
    stage('Build & Test')
    {        
        dir("${env.WORKSPACE}/javawebappbootcamp")
        {
            sh "${mavenCMD} clean install"  
            sh "${mavenCMD} package"
        }  
    }
    stage('Sonar Qube Test')
    {        
        dir("${env.WORKSPACE}/springbootappbootcamp")
        {
            sh "${mavenCMD} sonar:sonar"
        }  
    }
    stage('Build Docker Image')
   {      
        dir("${env.WORKSPACE}/javawebappbootcamp")
        {
            sh "${dockerCMD} build -t souravchtcs/javawebappbootcamp:1.0.0 ."
        }
   }
   stage('Push Docker Image')
   {      
        withCredentials([[$class: 'UsernamePasswordMultiBinding',credentialsId:'dockercred',usernameVariable:'USERNAME', passwordVariable:'PASSWORD']])
        {
            sh "${dockerCMD} login -u ${USERNAME} -p ${PASSWORD}"
            sh "${dockerCMD} push souravchtcs/javawebappbootcamp:1.0.0"
        }
   }
   stage('Create ec2 instance using Ansible')
   {
        sh "pip list|grep boto"
        dir("${env.WORKSPACE}/javawebappbootcamp")
        {
        ansiblePlaybook becomeUser: 'ubuntu', credentialsId: 'bootcampdevops', installation: 'ansible-1', playbook: 'ansible_web.yml', sudoUser: 'ubuntu'
        sleep(60)
        }
   }
   stage('Get ec2 instance')
   {
       def cliCommand = 'aws ec2 describe-instances  --query "reverse(sort_by(Reservations[].Instances[], &LaunchTime))[:1].PublicIpAddress[]" --region us-east-2'
       def output = sh script: "${cliCommand}" , returnStdout:true
       def ipList = output.split('"')
       ipAddress=ipList[1]
       println ipAddress
   }
   stage('Install & Start Docker')
   {
       def update = 'sudo apt-get update'
       def dockerInstall = 'sudo apt install docker.io -y'
       def dockerStart = 'sudo systemctl start docker'
       sshagent(['bootcampdevops'])
       {
       sh "ssh -o StrictHostKeyChecking=no ubuntu@${ipAddress} ${update}"
       sh "ssh -o StrictHostKeyChecking=no ubuntu@${ipAddress} ${dockerInstall}"
       sh "ssh -o StrictHostKeyChecking=no ubuntu@${ipAddress} ${dockerStart}"
       }
   }
   stage('Pull & Run Docker Image')
   {
       sshagent(['bootcampdevops'])
       {
           withCredentials([[$class: 'UsernamePasswordMultiBinding',credentialsId:'dockercred',usernameVariable:'USERNAME', passwordVariable:'PASSWORD']])
           {
            sh "ssh -o StrictHostKeyChecking=no ubuntu@${ipAddress} sudo docker login -u ${USERNAME} -p ${PASSWORD}"
            sh "ssh -o StrictHostKeyChecking=no ubuntu@${ipAddress} sudo docker pull souravchtcs/javawebappbootcamp:1.0.0"
            sh "ssh -o StrictHostKeyChecking=no ubuntu@${ipAddress} sudo docker run -p 9095:8080 -d souravchtcs/javawebappbootcamp:1.0.0"
           }
       }
   }
}
   catch(e)
   {
       emailext attachLog: true, body: "Jenkin Job Failed: Pipeline ${env.JOB_NAME}: Build ${env.BUILD_NUMBER}.Please find attached console log.Check console output at ${env.BUILD_URL}", compressLog: true, from:"JenkinsServer<souravchdevops@gmail.com>", subject: "Jenkins Job Failed: Pipeline ${env.JOB_NAME}: Build ${env.BUILD_NUMBER}", to: 'souravchdevops@gmail.com'
       throw e
   }
}
