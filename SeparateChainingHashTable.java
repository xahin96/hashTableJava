package hashTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static hashTable.GenerateRandomString.genRandStr;

// SeparateChaining Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// void makeEmpty( )      --> Remove all items

/**
 * Separate chaining table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class SeparateChainingHashTable<AnyType>
{
    /**
     * Construct the hash table.
     */
    public SeparateChainingHashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size approximate table size.
     */
    public SeparateChainingHashTable( int size )
    {
        theLists = new LinkedList[ nextPrime( size ) ];
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ] = new LinkedList<>( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, then do nothing.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        if( !whichList.contains( x ) )
        {
            whichList.add( x );

                // Rehash; see Section 5.5
            if( ++currentSize > theLists.length )
                rehash( );
        }
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        if( whichList.contains( x ) )
    {
        whichList.remove( x );
            currentSize--;
    }
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return true if x isnot found.
     */
    public boolean contains( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        return whichList.contains( x );
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ].clear( );
        currentSize = 0;    
    }

    /**
     * A hash routine for String objects.
     * @param key the String to hash.
     * @param tableSize the size of the hash table.
     * @return the hash value.
     */
    public static int hash( String key, int tableSize )
    {
        int hashVal = 0;

        for( int i = 0; i < key.length( ); i++ )
            hashVal = 37 * hashVal + key.charAt( i );

        hashVal %= tableSize;
        if( hashVal < 0 )
            hashVal += tableSize;

        return hashVal;
    }

    private void rehash( )
    {
        List<AnyType> [ ]  oldLists = theLists;

            // Create new double-sized, empty table
        theLists = new List[ nextPrime( 2 * theLists.length ) ];
        for( int j = 0; j < theLists.length; j++ )
            theLists[ j ] = new LinkedList<>( );

            // Copy table over
        currentSize = 0;
        for( List<AnyType> list : oldLists )
            for( AnyType item : list )
                insert( item );
    }

    private int myhash( AnyType x )
    {
        int hashVal = x.hashCode( );

        hashVal %= theLists.length;
        if( hashVal < 0 )
            hashVal += theLists.length;

        return hashVal;
    }
    
    private static final int DEFAULT_TABLE_SIZE = 101;

        /** The array of Lists. */
    private List<AnyType> [ ] theLists; 
    private int currentSize;

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }


        // Simple main
    public static void main( String [ ] args )
    {

        final int NUMS = 16;

        // Outer for loop for looping 15 times
        for(int i = 1; i < NUMS; i++)
        {
            SeparateChainingHashTable<String> H = new SeparateChainingHashTable<>( );

            // Simple list for calculating average time for hash insertion
            ArrayList<Double> timeElapsedInsertList = new ArrayList<Double>();

            // list that will hold N number of random string for
            // searching in the created hash later on section 2
            ArrayList<String> listOfRandStr = new ArrayList<String>();

            // Inner for loop for looping 2^i times
            for(int j = 0; j < Math.pow(2, i); j++)
            {
                // Generating a random string list for part 2
                listOfRandStr.add( genRandStr() );

                double startTime = System.currentTimeMillis( );
                H.insert( genRandStr() ); // Hash insertion
                double endTime = System.currentTimeMillis( );
                double timeElapsed = endTime - startTime;
                timeElapsedInsertList.add(timeElapsed);
            }

            // Calculating average time elapsed
            double totalTimeElapsed = 0;
            for(int k = 0; k < timeElapsedInsertList.size(); k++)
                totalTimeElapsed += timeElapsedInsertList.get(k);
            System.out.println("Total time elapsed inserting: " + totalTimeElapsed);


            // Simple list for calculating average time for hash search and delete
            ArrayList<Double> timeElapsedSearchDeleteList = new ArrayList<Double>();

            // Iterating over all the in the string list for searching in hash H
            for (String str : listOfRandStr)
            {
                double startTime = System.currentTimeMillis( );
                if ( H.contains( str ) )
                {
                    H.remove( str );
                }
                double endTime = System.currentTimeMillis( );
                double timeElapsed = endTime - startTime;
                timeElapsedSearchDeleteList.add(timeElapsed);
            }

            // Calculating average time elapsed
            double totalTimeElapsedSearchDelete = 0;
            for(int k = 0; k < timeElapsedSearchDeleteList.size(); k++)
                totalTimeElapsedSearchDelete += timeElapsedSearchDeleteList.get(k);
            System.out.println("Total time elapsed searching and deleting: " + totalTimeElapsedSearchDelete);
        }
    }
}
