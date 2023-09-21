package hashTable;


//QuadraticProbing Hash table class
//
//CONSTRUCTION: an approximate initial size or default of 101
//
//******************PUBLIC OPERATIONS*********************
//bool insert( x )       --> Insert x
//bool remove( x )       --> Remove x
//bool contains( x )     --> Return true if x is present
//void makeEmpty( )      --> Remove all items


import java.util.ArrayList;

import static hashTable.GenerateRandomString.genRandStr;

/**
* Probing table implementation of hash tables.
* Note that all "matching" is based on the equals method.
* @author Mark Allen Weiss
*/
public class QuadraticProbingHashTable<AnyType>
{
 /**
  * Construct the hash table.
  */
 public QuadraticProbingHashTable( )
 {
     this( DEFAULT_TABLE_SIZE );
 }

 /**
  * Construct the hash table.
  * @param size the approximate initial size.
  */
 public QuadraticProbingHashTable( int size )
 {
     allocateArray( size );
     doClear( );
 }

 /**
  * Insert into the hash table. If the item is
  * already present, do nothing.
  * @param x the item to insert.
  */
 public boolean insert( AnyType x )
 {
         // Insert x as active
     int currentPos = findPos( x );
     if( isActive( currentPos ) )
         return false;

     array[ currentPos ] = new HashEntry<>( x, true );
     theSize++;
     
         // Rehash; see Section 5.5
     if( ++occupied > array.length / 2 )
         rehash( );
     
     return true;
 }

 /**
  * Expand the hash table.
  */
 private void rehash( )
 {
     HashEntry<AnyType> [ ] oldArray = array;

         // Create a new double-sized, empty table
     allocateArray( 2 * oldArray.length );
     occupied = 0;
     theSize = 0;

         // Copy table over
     for( HashEntry<AnyType> entry : oldArray )
         if( entry != null && entry.isActive )
             insert( entry.element );
 }

 /**
  * Method that performs quadratic probing resolution.
  * @param x the item to search for.
  * @return the position where the search terminates.
  */
 private int findPos( AnyType x )
 {
     int offset = 1;
     int currentPos = myhash( x );
     
     while( array[ currentPos ] != null &&
             !array[ currentPos ].element.equals( x ) )
     {
         currentPos += offset;  // Compute ith probe
         offset += 2;
         if( currentPos >= array.length )
             currentPos -= array.length;
     }
     
     return currentPos;
 }

 /**
  * Remove from the hash table.
  * @param x the item to remove.
  * @return true if item removed
  */
 public boolean remove( AnyType x )
 {
     int currentPos = findPos( x );
     if( isActive( currentPos ) )
     {
         array[ currentPos ].isActive = false;
         theSize--;
         return true;
     }
     else
         return false;
 }
 
 /**
  * Get current size.
  * @return the size.
  */
 public int size( )
 {
     return theSize;
 }
 
 /**
  * Get length of internal table.
  * @return the size.
  */
 public int capacity( )
 {
     return array.length;
 }

 /**
  * Find an item in the hash table.
  * @param x the item to search for.
  * @return the matching item.
  */
 public boolean contains( AnyType x )
 {
     int currentPos = findPos( x );
     return isActive( currentPos );
 }

 /**
  * Return true if currentPos exists and is active.
  * @param currentPos the result of a call to findPos.
  * @return true if currentPos is active.
  */
 private boolean isActive( int currentPos )
 {
     return array[ currentPos ] != null && array[ currentPos ].isActive;
 }

 /**
  * Make the hash table logically empty.
  */
 public void makeEmpty( )
 {
     doClear( );
 }

 private void doClear( )
 {
     occupied = 0;
     for( int i = 0; i < array.length; i++ )
         array[ i ] = null;
 }
 
 private int myhash( AnyType x )
 {
     int hashVal = x.hashCode( );

     hashVal %= array.length;
     if( hashVal < 0 )
         hashVal += array.length;

     return hashVal;
 }
 
 private static class HashEntry<AnyType>
 {
     public AnyType  element;   // the element
     public boolean isActive;  // false if marked deleted

     public HashEntry( AnyType e )
     {
         this( e, true );
     }

     public HashEntry( AnyType e, boolean i )
     {
         element  = e;
         isActive = i;
     }
 }

 private static final int DEFAULT_TABLE_SIZE = 101;

 private HashEntry<AnyType> [ ] array; // The array of elements
 private int occupied;                 // The number of occupied cells
 private int theSize;                  // Current size

 /**
  * Internal method to allocate array.
  * @param arraySize the size of the array.
  */
 private void allocateArray( int arraySize )
 {
     array = new HashEntry[ nextPrime( arraySize ) ];
 }

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


//Simple main
public static void main( String [ ] args )
{
    final int NUMS = 16;

    // Outer for loop for looping 15 times
    for(int i = 1; i < NUMS; i++)
    {
        QuadraticProbingHashTable<String> H = new QuadraticProbingHashTable<>( );

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

