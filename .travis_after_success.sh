#!/bin/bash
#
# TecSinapse Exporter
#
# License: GNU Lesser General Public License (LGPL), version 3 or later
# See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
#

if [[ $TRAVIS_JDK_VERSION != "oraclejdk7" ]]; then
    echo "Skipping after_success actions for JDK version \"${TRAVIS_JDK_VERSION}\""
    exit $?
fi

mvn -B jacoco:report coveralls:report

if [[ -n $TRAVIS_TAG ]]; then
    echo "Skipping deployment for tag \"${TRAVIS_TAG}\""
    exit $?
fi

if [[ $TRAVIS_PULL_REQUEST == "false" && $TRAVIS_BRANCH == "milestone-v1.6" ]]; then
    mvn -B deploy -Dmaven.test.skip=true -Dfindbugs.skip=true -DperformRelease=false --settings $GPG_DIR/settings.xml
    exit $?
fi

if [[ $TRAVIS_BRANCH != "master" ]]; then
    echo "Skipping deployment for branch \"${TRAVIS_BRANCH}\""
    exit $?
fi

if [[ $TRAVIS_PULL_REQUEST == "false" ]]; then
    mvn -B deploy -Dmaven.test.skip=true -Dfindbugs.skip=true -DperformRelease=true --settings $GPG_DIR/settings.xml
    exit $?
fi
