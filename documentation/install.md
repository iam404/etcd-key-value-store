 # Install Documnetation

 # Prerequisite

  * [Minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)
  * [Kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
  * Postman/Curl (optional)

 # Deployment

    chmod +x ./deploy.sh && ./deploy.sh


### Namespace

All kubernetes resources are deployed to a seperate namespace `demo-iam404`



# Notes

##### Check pod status
    kubectl get pods -n demo-iam404

##### Check services status
    kubectl get services -n demo-iam404

##### Check volume claim status
    kubectl get pvc -n demo-iam404
