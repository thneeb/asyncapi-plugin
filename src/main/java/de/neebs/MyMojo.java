package de.neebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 */
@Mojo( name = "asyncapi-generator", defaultPhase = LifecyclePhase.GENERATE_SOURCES )
public class MyMojo extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "sourceFolder", required = true )
    private String sourceFolder;

    @Parameter( property = "inputSpec", required = true)
    private String inputSpec;

    @Parameter( property = "apiPackage", required = true)
    private String apiPackage;

    @Parameter( property = "modelPackage", required = true)
    private String modePackage;

    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        GeneratorConfig generatorConfig = new GeneratorConfig();
        generatorConfig.setSourceFolder(sourceFolder);
        generatorConfig.setInputSpec(inputSpec);
        generatorConfig.setApiPackage(apiPackage);
        generatorConfig.setModelPackage(modePackage);
        AsyncApiGenerator swaggerMixer = new AsyncApiGenerator(objectMapper, yamlObjectMapper);
        swaggerMixer.run(generatorConfig);
    }
}
