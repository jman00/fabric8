## Get Started with Fabric8 and Jube

First you need to get the latest [Jube](jube.html) by [downloading jube-2.0.0-image.zip](http://central.maven.org/maven2/io/fabric8/jube/images/jube/jube/2.0.0/jube-2.0.0-image.zip) and unzipping it.

You can then startup Jube via:

    cd jube-2.0.0-image
    ./run.sh

If your operating system does't have the executable flag set on the run script; try

    chmod +x *.sh *.bat bin/*

To stop Jube hit **Ctrl-C**.

If you want to run Jube in the background you can run **./start.sh** then you can run **./stop.sh** to stop it or run **./status.sh** to see if its still running.

### Setting environment variables

The following environment variables are used by various [tools](http://fabric8.io/v2/tools.html) such as the [maven plugin](http://fabric8.io/v2/mavenPlugin.html), the [Forge Addons](http://fabric8.io/v2/forge.html) and the [java libraries](javaLibraries.html):

    export KUBERNETES_MASTER=http://localhost:8585/
    export FABRIC8_CONSOLE=http://localhost:8585/hawtio/

Once you have set those in your shell (or ~/.bashrc) you can then use all the fabric8 tools against your local Jube node.

### Using the web console

Once Jube has started up you should be able to open the web console at [http://localhost:8585/hawtio/](http://localhost:8585/hawtio/) to view the kubernetes system. You can then view these tabs:

 * [Pods tab](http://localhost:8585/hawtio/kubernetes/pods) views all the available [pods](pods.html) in your kubernetes environment
 * [Replication Controllers tab](http://localhost:8585/hawtio/kubernetes/replicationControllers) views all the available [replication controllers](replicationControllers.html) in your kubernetes environment
 * [Services tab](http://localhost:8585/hawtio/kubernetes/services) views all the available [services](services.html) in your kubernetes environment

### Running a quickstart application

You can use any kubernetes client such as the [Forge Addons](http://fabric8.io/v2/forge.html) to apply kubernetes JSON files for [pods](pods.html), [replication controllers](replicationControllers.html) or [services](services.html) - provided that any docker images referenced in the pods and pod templates have a suitable [image zip](http://fabric8.io/jube/imageZips.html).

In the following example we're going to use the [maven plugin's fabric8:run goal](http://localhost:4000/mavenPlugin.html#running) to run the [generated kubernetes JSON](http://localhost:4000/mavenPlugin.html#generating-the-json) file using Jube.

    git clone https://github.com/fabric8io/quickstarts.git
    cd quickstarts/java/camel-spring
    mvn clean install fabric8:run

Note if you are trying this on your own maven project you may wish to add the [jube:build goal to your projects package goal](http://fabric8.io/jube/mavenPlugin.html#adding-the-plugin-to-your-project) or you can run the **jube:build** goal via

    mvn clean install jube:build fabric8:run

The build will [generated](http://localhost:4000/mavenPlugin.html#generating-the-json) a **target/classes/kubernetes.json** file as part of the build (the **fabric8:json** goal does that), then the **fabric8:run** goal will use the Kubernetes REST API to create the necessary [pods](pods.html), [Replication Controllers](replicationControllers.html) or [services](services.html).

Once you have run the **mvn fabric8:run** command you should see it succeed. If you hit any issues check out the [FAQ](http://fabric8.io/v2/FAQ.html), [get in touch](http://fabric8.io/community/index.html) or [raise an issue](https://github.com/fabric8io/fabric8/issues)

Now you should be able to see any created kubernetes resources in the tabs: [Pods](http://localhost:8585/hawtio/kubernetes/pods), [Replication Controllers](http://localhost:8585/hawtio/kubernetes/replicationControllers) or [Services](http://localhost:8585/hawtio/kubernetes/services) in the web console. Note that the first thing to be created are the [replication controllers](replicationControllers.html) which then try to start the pods.

#### If a pod fails to start

Sometimes you may see a pod not start; it may then try again and create more pods. If you hover over the status icon on the [Pods tab](http://localhost:8585/hawtio/kubernetes/pods) you should see a tooltip explanation for the failure (or click on the pod and look at the detail view).

Usually this means that Jube could not find the [image zip](http://fabric8.io/jube/imageZips.html) in a maven repository for the docker container names referenced in your pod JSON. You may want to check that you properly built the associated image zip and its available in a maven repository at the maven coordinates that the exception reports it used.

#### Scaling your replication controllers

When you are running some [replication controllers](replicationControllers.html) you can use the [Replication Controllers tab](http://localhost:8585/hawtio/kubernetes/replicationControllers) and update the replica count (then hit **Save**) and in a few moments you should see Jube create or destroy pods so that the current running system matches your required number of replicas.

#### Stopping pods

The easiest way to stop pods is to set the number of replicas to 0 on the [Replication Controllers tab](http://localhost:8585/hawtio/kubernetes/replicationControllers). Then delete the replication controller if you wish.
