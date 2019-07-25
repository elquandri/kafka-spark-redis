# kafka-spark-redis
ensure good connectivity between Apche Kafka and Apache Spark and Redis " consume selected topic data  / process the data consume with spark /  write the data in Redis / validate test"


#	Application Interaction Kafka/Spark/Redis

Cette application a pour but tester les interactions entre Apache kafka et Apache Spark et Redis, elle permet :

-	La consommation des données depuis un topic Kafka.
-	Faire des traitements Spark sur ces données.
-	L’écriture des données traitées dans Redis.
-	La lecture des données écrit en s’appuyant sur les Keys.
-	La vérification des données écrit/lu.
-	Générer un fichier résultat en format JSON, qui contient les informations du test et son résultat. 
#	Composants concernés


Composant	  	Version
Spark		2.3.2
Kafka		1.1.1
Redis		4.0.14
DC-OS		1.12
Scala		2.11.8
Assembly		0.14.9
Gitlab		11.3.0



# Prérequis 
-	 Avant de lancer l’application vous devez la configurer, cela se fait au niveau du fichier de configuration de l’application, qui est dans le chemin (/src/main/resources/KafkaRedis.conf).


# Traitements 
-	Lancer l’application sur le dcos bootstrap avec la commande 
dcos spark --name="spark-2-3" run --submit-args="--conf spark.mesos.containerizer=docker --conf spark.driver.memory=4G --conf spark.cores.max=3 --conf spark.executor.cores=1 --conf spark.executor.memory=4G --conf spark.mesos.executor.docker.forcePullImage=false --conf spark.eventLog.enabled=true --conf spark.eventLog.dir=hdfs:///spark_history  --class kafkaToRedis hdfs:///jars/Kafka-Spark-Redis-assembly-0.1.jar"
 
# Validation du test 
- Vérifier le statut du test dans le fichier résultat. 
