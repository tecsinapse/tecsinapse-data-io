#!/bin/bash

if [[ "${TRAVIS_JDK_VERSION}" != "oraclejdk7" ]]; then
    echo "Skipping after_success actions for JDK version \"${TRAVIS_JDK_VERSION}\""
    exit
fi

mvn -B clean test jacoco:report coveralls:report

if [[ -n ${TRAVIS_TAG} ]]; then
    echo "Skipping deployment for tag \"${TRAVIS_TAG}\""
    exit
fi

if [[ ${TRAVIS_BRANCH} != 'master' ]]; then
    echo "Skipping deployment for branch \"${TRAVIS_BRANCH}\""
    exit
fi

if [[ "$TRAVIS_PULL_REQUEST" = "true" ]]; then
    echo "Skipping deployment for pull request"
    exit
fi

echo "<settings><servers><server><id>sonatype-nexus</id><username>\${env.SONATYPE_USERNAME}</username><password>\${env.SONATYPE_PASSWORD}</password></server></servers></settings>" > ~/settings.xml

mvn -B deploy -Dmaven.test.skip=true -DperformRelease=true --settings ~/settings.xml
