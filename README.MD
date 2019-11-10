# aws-s3

Quarkus Extension: Amazon S3 Storage

# installation

`mvn install`

# usage

Add the following dependency to a quarkus project `pom.xml`:

```xml
<dependency>
    <groupId>nz.fiore</groupId>
    <artifactId>quarkus-aws-s3</artifactId>
    <version>999-SNAPSHOT</version>
</dependency>
```
# And after you can use code like:


`
 @ConfigProperty(name = "minio.endpoint")
    String endpoint;

    @ConfigProperty(name = "amazonwss3.access_key")
    String accessKey;

    @ConfigProperty(name = "amazonwss3.secret_key")
    String secretKey;

    @ConfigProperty(name = "amazonwss3.bucket_name")
    String bucket_name;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        if (s3client.doesBucketExist(bucket_name)) {
            logger.info("Bucket name is not available."
                    + " Try again with a different Bucket name.");
        } else {
            s3client.createBucket(bucket_name);
        }

        s3client.putObject(
                bucket_name,
                "hello.txt",
                new File("/hello.txt")
        );
        ObjectListing objectListing = s3client.listObjects(bucket_name);
        for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
            logger.info(os.getKey());
        }
`

See you at: [airhacks.com](http://airhacks.com), 