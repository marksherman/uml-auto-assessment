package student.web.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import student.web.internal.TemplateManager.Template.Field;


public class TemplateManager
{
    private static TemplateManager TM = new TemplateManager();

    private List<Template> templates = new ArrayList<Template>();

    private File templateStorageLocation;

    private String TEMP_EXT = ".template";

    private boolean enabled = false;

    private static String NULLABLE = "nullable";


    // private Logger logger = Logger.getLogger(TemplateManager.class);

    public class Template
    {
        public class Field
        {
            String name;

            boolean nullable;


            public Field( String myName, boolean myNull )
            {
                name = myName;
                nullable = myNull;
            }


            public String getName()
            {
                return name;
            }


            public boolean isNullable()
            {
                return nullable;
            }


            public void setNullable( boolean checked )
            {
                nullable = checked;
            }
        }

        private String name;

        private List<Field> fields = new ArrayList<Field>();

        private Field defaultField;


        public Template( String myName )
        {
            name = myName;
            defaultField = new Field( "_DEFAULT", true );
        }


        public Template( File persistedTemplate )
        {
            name = persistedTemplate.getName().substring( 0,
                persistedTemplate.getName().indexOf( ".template" ) );
            Properties prop = new Properties();
            try
            {
                prop.load( new FileReader( persistedTemplate ) );
                for ( Object key : prop.keySet() )
                {
                    String fieldName = splitFieldName( (String)key );
                    // String attribute = splitAttribute((String)key);
                    boolean nullable = new Boolean( prop.getProperty( (String)key ) );
                    if ( fieldName.equals( "_DEFAULT" ) )
                    {
                        defaultField = new Field( fieldName, nullable );
                    }
                    else
                    {
                        fields.add( new Field( fieldName, nullable ) );
                    }

                }
            }
            catch ( FileNotFoundException e )
            {
                // logger.error("Could not find the file specified in the template load "+persistedTemplate.getName(),
                // e);
            }
            catch ( IOException e )
            {
                // logger.error("Could not read the template file "+persistedTemplate.getName(),e);
            }
            if ( defaultField == null )
            {
                defaultField = new Field( "_DEFAULT", true );
            }

        }


        private String splitFieldName( String key )
        {
            return key.substring( 0, key.indexOf( "." ) );
        }


        private String splitAttribute( String key )
        {
            return key.substring( key.indexOf( "." ) );
        }


        public String getName()
        {
            return name;
        }


        public List<Field> getFields()
        {
            return fields;
        }


        public void addField( String text, boolean b )
        {
            fields.add( new Field( text, b ) );
            persistTemplate();

        }


        public void removeField( Field value )
        {
            fields.remove( value );
            persistTemplate();
        }


        public void persistTemplate()
        {
            Properties prop = new Properties();
            prop.setProperty( defaultField.getName() + "." + NULLABLE,
                new Boolean( defaultField.isNullable() ).toString() );
            for ( Field f : fields )
            {
                prop.setProperty( f.getName() + "." + NULLABLE,
                    new Boolean( f.isNullable() ).toString() );
            }
            try
            {
                FileWriter fwrite = new FileWriter( getPersistenceFile() );
                prop.store( fwrite, "Template File for " + this.name );
                fwrite.close();
            }
            catch ( IOException e )
            {
                // logger.error("Could not persist "+this.name, e);
            }
        }


        private File getPersistenceFile()
        {
            return new File( templateStorageLocation, this.name + TEMP_EXT );
        }


        @Override
        public boolean equals( Object o )
        {
            if ( o instanceof Template )
            {
                if ( ( (Template)o ).name.equals( this.name ) )
                {
                    return true;
                }
            }
            return false;
        }


        public void removePersistence()
        {
            if ( !getPersistenceFile().delete() )
            {
                // logger.error("Could not delete Persisted Template " +
                // this.name);
            }

        }


        public Field getField( String myField )
        {
            for ( Field temp : this.fields )
            {
                if ( temp.getName().equals( myField ) )
                {
                    return temp;
                }
            }
            return null;
        }


        public boolean containsKey( String classField )
        {
            for ( Field field : fields )
            {
                if ( field.getName().equals( classField ) )
                {
                    return true;
                }
            }
            return false;
        }


        public Field getDefault()
        {
            return this.defaultField;
        }


        public void setDefault( boolean isNullable )
        {
            defaultField.setNullable( isNullable );
        }
    }


    private TemplateManager()
    {
    }


    public void enableTemplateChecking( File storageLocation )
    {
        templateStorageLocation = storageLocation;
        enabled = true;
        rebuildTemplates();
    }


    public boolean isEnabled()
    {
        return enabled;
    }


    private void rebuildTemplates()
    {
        // Rebuild the template list.
        for ( File tempFile : templateStorageLocation.listFiles() )
        {
            if ( tempFile.getName().endsWith( ".template" ) )
            {
                templates.add( new Template( tempFile ) );
            }
        }

    }


    public static TemplateManager getInstance()
    {
        return TM;
    }


    public Template addTemplate( String text )
    {
        Template toAdd = new Template( text );
        if ( templates.contains( toAdd ) )
        {
            return null;
        }
        templates.add( toAdd );
        toAdd.persistTemplate();
        return toAdd;

    }


    public List<Template> getTemplates()
    {
        return templates;
    }


    public void removeTemplate( Template selected )
    {
        templates.remove( selected );
        selected.removePersistence();
    }


    public Template getTemplate( String source )
    {
        for ( Template temp : templates )
        {
            if ( temp.getName().equals( source ) )
            {
                return temp;
            }
        }
        return null;
    }
}
