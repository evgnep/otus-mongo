minikube start
minikube dashboard

minikube docker-env
Копируем и выполняем его выхлоп

docker build -t mongo_kub2_app:v1 .

kubectl create namespace mongo-kub2
kubectl config set-context --current --namespace=mongo-kub2



=========== ConfigSet

kub/1-config> kubectl apply -f app.yaml

kub/1-config> kubectl port-forward configmap-demo-pod 8000:80

меняем конфиг через дашбоард, проверяем выхлоп в браузере - нет изменений

рестарт пода
kubectl get pod configmap-demo-pod -o yaml | kubectl replace --force -f -

kubectl port-forward configmap-demo-pod 8000:80
смотрим конфиг

============= Secret

kub/1-config> kubectl delete -f app.yaml
kub/1-config> kubectl apply -f app-secret.yaml

kubectl create secret generic mysecret --from-literal=username=devuser --from-literal=password=secret_password
kubectl get secrets
kubectl describe secrets/mysecret
kubectl get secret mysecret -o jsonpath='{.data}'

https://www.base64decode.org/

kubectl port-forward configmap-demo-pod 8000:80
смотрим в браузере
смотрим в поде в дашборде - значения открыты

kubectl delete -f app-secret.yaml
kubectl delete secret mysecret

============== PersistentVolume

kub/2-persistentVolume> kubectl apply -f infra.yaml
kub/2-persistentVolume> kubectl apply -f deployment.yaml

minikube service persistent-service --url -n mongo-kub2

http://127.0.0.1:58973/
http://127.0.0.1:58973/set?data=some_data
http://127.0.0.1:58973/

minikube ssh
ls /tmp/hostpath-provisioner/exp/storage
cat /tmp/hostpath-provisioner/exp/storage/state.txt

============== StatefulSet

kub/2-persistentVolume> kubectl apply -f stateful.yaml
Смотрим поды и клаймы в дашбоарде

kubectl port-forward  statefulset-app-0  8000:80
kubectl port-forward  statefulset-app-1  8001:80

localhost:8000/set?data=data1
localhost:8001/set?data=data2

грохаем в дашбоарде statefulset-app-0, наблюдаем
грохаем под деплоймента, наблюдаем

=============== Mongo

kub/3-mongo> kubectl apply -f mongo.yaml

kub/3-mongo> kubectl apply -f app-config.yaml -f deployment.yaml -f service.yaml

minikube service mongo-service --url -n mongo-kub2


============ Helm

Почистим

kub/4-helm> helm create example-chart

https://artifacthub.io/packages/helm/bitnami/mongodb


helm install mongo-1 oci://registry-1.docker.io/bitnamicharts/mongodb
helm install mongo-2 oci://registry-1.docker.io/bitnamicharts/mongodb

helm list
helm uninstall app-dev
