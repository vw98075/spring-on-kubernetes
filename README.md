# Spring On Kubernetes

This is a demo project to illustrate the procedure of developing a microservices system with Spring Boot and Spring Cloud and deploying on Kubernetes. The procedure is based on the latest version of Spring Boot (2.3) to simplify the containerized deployment process.

## Steps

* Create a simple reactive Spring Boot app
* Build a Docker image for the app
* Push the app to a Docker repo
* Create deployment and service descriptors for Kubernetes
* Deploy and run the app on Kubernetes

### Prerequisites

* Java 8 or higer
* Spring 2.3 or higher
* Docker installment
* Kubernetes installment, including Kubectl which is one component of GCloud components

### Create A Spring Boot App

Omit all steps as they aren't the focus of this project.

## Containerize The App

### Building A Container
```
./mvnw spring-boot:build-image
```
It isn't need to create a Docker file anymore. The above command is only available for Spring Boot 2.3. 

To see the newly built Docker image, run
```
$ docker images
```
### Run The Container
```
docker run --name [app name] -p 8080:8080 [app name]:[version #]
```
### Test The App Responds

```
curl http://localhost:8080; echo
```

## Jib It!

* [Jib](https://github.com/GoogleContainerTools/jib) is a tool from by Google to make building Docker containers easier
* Runs as a Maven or Gradle plugin
* Does not require a Docker deamon
* Implements many Docker best practices automatically
* Add additional notes about how to deploy this on a live system

### Add Jib To The POM

### Using Jib To Build A Container

```
 ./mvnw clean compile jib:dockerBuild
```
NOTE: This still uses your local Docker dameon to build the container

## Putting The Container In A Registry

* Up until this point the container only lives on your machine
* It is useful to instead place the container in a registry
* Allows others to use the container
* Docker Hub is a popular public registry
* Private registries exist as well

## Using Jib To Push To A Registry

### Authenticate With The Registry

If you don't have Docker Hub default login setup, you need to login Docker Hub first

```
docker login --username=yourhubusername --email=youremail@company.com
```

### Modify Jib Plugin Configuration

### To put the container in Docker Hub

```
docker push [OPTIONS] NAME[:TAG]
```

### Run The Build And Deploy The Container

To run the Maven build which will deploy the container to your container registry,

```
./mvnw clean package
```

You should now see the image in your registry.

## Deploying To Kubernetes

With our container build and deployed to a registry we can now run this container on Kubernetes.

### Deployment Descriptor

* Kubernetes uses YAML files to provide a way of describing how the app will be deployed to the platform
* You can write these by hand using the Kubernetes documentation as a reference
* Or you can have Kubernetes generate it for you using kubectl
* The --dry-run flag allows us to generate the YAML without actually deploying anything to Kubernetes

```
mkdir k8s
kubectl create deployment [app name] --image docker.io/[Docker Hub user ID]/[app name] -o yaml --dry-run > k8s/deployment.yaml
```

### Service Descriptor

* A service acts as a load balancer for the pod created by the deployment descriptor
* If we want to be able to scale pods than we want to create a service for those pods

```
kubectl create  service clusterip [app name] --tcp 80:8080 -o yaml --dry-run ] k8s/service.yaml
```

### Apply The Deployment and Service YAML

* The deployment and service descriptors have been created in the /k8s directory
* Apply these to get everything running

If you have watch installed you can watch as the pods and services get created
```
watch -n 1 kubectl get all
```
In a separate terminal window run
```
kubectl apply -f ./k8s
```

### Testing The App

The service is assigned a cluster IP, which is only accessible from inside the cluster as shown in the watch output

```
NAME                   TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)   AGE
service/[app-name]   ClusterIP   10.100.200.243   <none>        80/TCP    68m
```
To access the app we can use kubectl port-forward

```
kubectl port-forward service/[app-name] 8080:80
```

Now we can curl localhost:8080 and it will be forwarded to the service in the cluster

```
curl http://localhost:8080; echo
```

