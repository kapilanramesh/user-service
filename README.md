# Spring Boot Microservice Deployment on Amazon EKS with Helm

This project demonstrates deploying a **Spring Boot-based Java microservice** to an **Amazon EKS cluster** using **Helm**. It involves building a Docker image, pushing it to Docker Hub, and using Helm charts for Kubernetes resource management.

---

## 📦 Tech Stack

* **Spring Boot** (Java-based Web App)
* **Docker** (Containerization using OpenJDK 17)
* **Amazon EKS** (Elastic Kubernetes Service)
* **Helm** (Kubernetes Package Manager)
* **kubectl** (Kubernetes CLI)
* **AWS CLI + IAM Roles**

---

## 🔧 Phase 1: Spring Boot App Setup

1. **Folder Structure**

```bash
springboot-app/
├── src/
│   └── main/
│       ├── java/com/example/users/
│       │   ├── controller/
│       │   ├── model/
│       │   ├── service/
│       │   └── UserServiceApplication.java
│       └── resources/
│           ├── application.properties
└── pom.xml
```

2. **Develop Spring Boot Application**

   * Web server built using Maven with a REST endpoint `/` returning a message.

3. **Build and Run Locally**

   ```bash
   mvn clean package
   java -jar target/user-service.jar
   ```

---

## 🐳 Phase 2: Dockerize Spring Boot App

1. **Dockerfile**

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/user-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

2. **Build & Push Image**

```bash
docker build -t docker.io/kapilan97/user-service .
docker push docker.io/kapilan97/user-service
```

---

## ☘️ Phase 3: EKS Cluster + Helm + K8s Deploy

1. **Create EKS Cluster using eksctl**

```bash
eksctl create cluster --name devops-eks-cluster --region us-east-1 --nodes 2
```

2. **Helm Chart Structure**

```bash
helm create user-service
```

3. **Update Helm files**

* `values.yaml`

```yaml
replicaCount: 2

image:
  repository: docker.io/kapilan97/user-service
  pullPolicy: IfNotPresent
  tag: latest

service:
  type: LoadBalancer
  port: 80
  targetPort: 8081

containerPort: 8081

ingress:
  enabled: true
  className: "nginx"
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
  hosts:
    - host: user-service.<EXTERNAL-IP>.nip.io
      paths:
        - path: /
          pathType: Prefix
  tls: []
```

* `deployment.yaml` — Adjust `containerPort`, image, and selector labels
* `service.yaml` — Match selector and ports with `values.yaml`

---

## 🚀 Phase 4: Helm Deployment

1. **Install NGINX Ingress Controller**

```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm install ingress-nginx ingress-nginx/ingress-nginx
```

2. **Install Helm App**

```bash
helm install user-service ./user-service
```

3. **Update/Upgrade**

```bash
helm upgrade user-service ./user-service
```

---

## 🌐 Accessing the App

1. **Get EXTERNAL-IP of the LoadBalancer**

```bash
kubectl get svc user-service
```

2. **Form nip.io domain**

```text
http://user-service.<EXTERNAL-IP>.nip.io
```

3. **Open in browser** → should see Spring Boot welcome message

---

## 📄 Outcome Summary

* Spring Boot app containerized using OpenJDK 17 and deployed to EKS
* Helm used for reusable deployment configs
* NGINX Ingress + `nip.io` enabled friendly external DNS access
* LoadBalancer service exposed microservice to the public

---

