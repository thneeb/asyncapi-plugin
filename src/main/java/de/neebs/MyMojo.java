package de.neebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.maven.plugin.AbstractMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

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
    private String modelPackage;

    @Parameter( property = "avro", required = false, defaultValue = "false")
    private boolean avro;

    @Parameter( property = "spring", required = false, defaultValue = "true")
    private boolean spring;

    @Parameter(defaultValue = "true", property = "addCompileSourceRoot")
    private boolean addCompileSourceRoot;

    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    public void execute() {
        addCompileSourceRootIfConfigured();
        Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
        b.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX"));
        b.failOnUnknownProperties(false);
        ObjectMapper objectMapper = b.build();
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
        GeneratorConfig generatorConfig = new GeneratorConfig();
        generatorConfig.setSourceFolder(sourceFolder);
        generatorConfig.setInputSpec(inputSpec);
        generatorConfig.setApiPackage(apiPackage);
        generatorConfig.setModelPackage(modelPackage);
        generatorConfig.setAvro(avro);
        generatorConfig.setSpring(spring);
        AsyncApiGenerator swaggerMixer = new AsyncApiGenerator(objectMapper, yamlObjectMapper);
        swaggerMixer.run(generatorConfig);
    }

    private void addCompileSourceRootIfConfigured() {
        if (addCompileSourceRoot) {
            project.addCompileSourceRoot(sourceFolder);
        }
    }
}
