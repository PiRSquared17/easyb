package org.easyb.maven;

import java.util.Locale;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.disco.easyb.util.CamelCaseConverter;

/**
 * Execute story specifications defined with easyb
 *
 * @goal report
 * @phase site
 */
public class StoryReportMojo extends AbstractMavenReport {
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    @SuppressWarnings("UnusedDeclaration")
    private MavenProject project;

    /**
     * @component
     */
    @SuppressWarnings("UnusedDeclaration")
    private Renderer siteRenderer;

    /**
     * @parameter expression="${project.build.directory}/easyb-stories"
     * @required
     */
    @SuppressWarnings("UnusedDeclaration")
    private File outputDirectory;

    /**
     * @parameter expression="${project.build.directory}/easyb/report.xml"
     * @required
     */
    @SuppressWarnings("UnusedDeclaration")
    private File easybReport;

    @SuppressWarnings("ThrowFromFinallyBlock")
    protected void executeReport(Locale locale) throws MavenReportException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        FileInputStream reportStream = null;
        try {
            reportStream = new FileInputStream(easybReport);
            EasybReportReader reader = new EasybReportReader(reportStream);
            for (Story story : (List<Story>)reader.getStories()) {
                try {
                    CamelCaseConverter converter = new CamelCaseConverter(story.getName());
                    new File(outputDirectory, converter.toCamelCase() + "Story.html").createNewFile();
                } catch (IOException e) {
                    throw new MavenReportException("Unable to create story file", e);
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new MavenReportException("Unable to read easyb report", e);
        }
        finally {
            if (reportStream != null) {
                try {
                    reportStream.close();
                } catch (IOException e) {
                    throw new MavenReportException("Unable to close easyb report", e);
                }
            }
        }
    }

    protected Renderer getSiteRenderer() {
        return siteRenderer;
    }

    protected String getOutputDirectory() {
        return outputDirectory.getAbsolutePath();
    }

    protected MavenProject getProject() {
        return project;
    }

    public String getOutputName() {
        return "easyb-stories";
    }

    public String getName(Locale locale) {
        return "Easyb Story Reports";
    }

    public String getDescription(Locale locale) {
        return "Easyb Story Reports";
    }
}