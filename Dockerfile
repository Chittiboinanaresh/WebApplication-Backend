FROM airhacks/glassfish
COPY ./target/pw-secondi.war ${DEPLOYMENT_DIR}
