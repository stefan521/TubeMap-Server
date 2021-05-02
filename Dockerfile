FROM openjdk:8-jre
WORKDIR /tube-server
COPY svc /svc
EXPOSE 9000 9443
CMD /svc/bin/start -Dhttps.port=9443 -Dplay.crypto.secret=secret
ENV TUBE_API_KEY=abddd
