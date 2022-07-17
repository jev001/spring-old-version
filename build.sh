#! /bin/bash

# replace JAVA_HOME with jdk1.4
# export JAVA_HOME_4=~/downloads/j2sdk1.4.2_19

# $1 param
## eg: modulejars package all module
## eg: srczip package all source to zip


if [ -n "$JAVA_HOME_4" ]; then
  export JAVA_HOME=$JAVA_HOME_4
  export PATH=$PATH:$JAVA_HOME/bin
fi

jvm_jar=$JAVA_HOME/lib/tools.jar
ant_jar=./lib/ant/ant.jar:./lib/ant/ant-launcher.jar:./lib/ant/ant-trax.jar:./lib/ant/ant-junit.jar
junit_jar=./lib/junit/junit.jar
clover_jar=./lib/clover/clover.jar


startup_jar=$jvm_jar:$ant_jar:$junit_jar:$clover_jar
$JAVA_HOME/bin/java -cp $startup_jar  org.apache.tools.ant.Main $1