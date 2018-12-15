package repository;

import domain.HasID;
import domain.Validator.ValidationException;
import domain.Validator.Validator;

import java.io.*;

public abstract class InFileRepo<ID, E extends HasID<ID>> extends InMemoryRepo<ID,E> {
    String fileName;
    public InFileRepo(Validator validator,String file) {
        super(validator);
        this.fileName=file;
    }
    @Override
    /**
     * same as the in memory, but the changed are saved in file too
      */
    public E update(E entity) throws ValidationException, IllegalArgumentException, FileNotFoundException {
        E old=findOne(entity.getID());
        super.update(entity);

            rewriteFile();


        return old;
    }

    /**
     *
     * @param id the id of the element that will be deleted
     * id must be not null
     * @return the element, if deleted, null otherwise
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    @Override
    public E delete(ID id) throws IllegalArgumentException, FileNotFoundException {
        E elem=super.delete(id);
                try{rewriteFile();}
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return elem;
    }

    /**
     * rewrites the whole file of the repo
     * @throws FileNotFoundException
     */
    protected void rewriteFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        writer.close();
        entities.values().forEach(this::writeToFile);
    }

    /**
     * Reads the data from the file
     */
    protected void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = br.readLine())!= null){
                E temp = extractEntity(line);
                if (temp!=null)
                super.save(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        try {
            rewriteFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param entity the element that will be added in repo
     * entity must be not null
     * @return null if succesfull, the entity if fails, entity added in file too
     * @throws ValidationException
     * @throws IllegalArgumentException
     */
    @Override
    public E save(E entity)throws ValidationException,IllegalArgumentException {
        E returnedEntity = super.save(entity);
        if(returnedEntity == null) {
            writeToFile(entity);
            return null;
        }
        return returnedEntity;
    }

    /**
     *
     * @param line a line from the text file
     * @return the entity that is stored in the repo
     *
     */
    protected abstract E extractEntity(String line);
    public void writeToFile(E entity){
        try(BufferedWriter br=new BufferedWriter((new FileWriter(fileName,true))))
        {
            br.write(entity.toString());
            br.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
