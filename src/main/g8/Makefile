ARTEFACT_FILE := ./target/scala-3.*/*-assembly-*.jar
REST_STUBS_LOCATION := target/rest-stubs
# Check if the dependency file exists
ARTEFACT_EXISTS := \$(wildcard \$(ARTEFACT_FILE))

ifeq (\$(ARTEFACT_EXISTS),)
PACKAGE_DEPENDENCY := package
else
PACKAGE_DEPENDENCY :=
endif

# packages up / locally publishes the model jar 
# which can then be used from build.sbt
#
# you may want to publish this "properly" as part of your CI/CD pipeline, 
# and consider using -SNAPSHOT versions
#
packageRestCode: generateRestCode
	cd \$(REST_STUBS_LOCATION) && sbt publishLocal

# generate the REST services from our ./service.yaml into a target directory
generateRestCode:
	# there are often changes to the scala-cask template, so it's useful to pull when needed:
	docker pull openapitools/openapi-generator-cli:latest
	docker run --rm -v \${PWD}:/local openapitools/openapi-generator-cli:latest generate \
        -i /local/service.yaml \
        -g scala-cask \
        -c /local/openapi-config.yaml \
        -o /local/\$(REST_STUBS_LOCATION)
