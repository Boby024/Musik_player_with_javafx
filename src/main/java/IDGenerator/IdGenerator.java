package IDGenerator;

public class IdGenerator  {
    private static long id = -1 ;

    public static long getNextID() throws  IDOverFlowException{
        if (id <=9999){
            ++id;
            return id ;
        }else{
            throw new IDOverFlowException();
        }

    }
}
