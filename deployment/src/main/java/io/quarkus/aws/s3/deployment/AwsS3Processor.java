package io.quarkus.aws.s3.deployment;

import com.amazonaws.partitions.model.*;
import com.amazonaws.services.s3.internal.AWSS3V4Signer;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageProxyDefinitionBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.builditem.nativeimage.RuntimeInitializedClassBuildItem;

class AwsS3Processor {

    private static final String FEATURE = "aws-s3";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }


    @BuildStep
    ExtensionSslNativeSupportBuildItem activateSslNativeSupport() {
        return new ExtensionSslNativeSupportBuildItem(FEATURE);
    }

    @BuildStep
    RuntimeInitializedClassBuildItem runtime() {
        return new RuntimeInitializedClassBuildItem(CryptoConfiguration.class.getCanonicalName());
    }

    @BuildStep
    NativeImageProxyDefinitionBuildItem proxies() {
        return new NativeImageProxyDefinitionBuildItem(
                "org.apache.http.conn.HttpClientConnectionManager",
                "org.apache.http.pool.ConnPoolControl",
                "com.amazonaws.http.conn.Wrapped");
    }

    @BuildStep
    NativeImageResourceBuildItem resources() {
        //new NativeImageResourceBuildItem("com/amazonaws/partitions/endpoints.json"));
        //mime.types
        return new NativeImageResourceBuildItem("com/amazonaws/partitions/endpoints.json", "mime.types");
    }

    @BuildStep
    ReflectiveClassBuildItem reflective() {
        return new ReflectiveClassBuildItem(true, false,
                Partitions.class.getCanonicalName(),
                Partition.class.getCanonicalName(),
                Endpoint.class.getCanonicalName(),
                Region.class.getCanonicalName(),
                Service.class.getCanonicalName(),
                CredentialScope.class.getCanonicalName(),
                "org.apache.commons.logging.impl.LogFactoryImpl",
                "org.apache.commons.logging.impl.Jdk14Logger",
                "com.sun.org.apache.xerces.internal.parsers.SAXParser",
                AWSS3V4Signer.class.getCanonicalName());
    }

}
