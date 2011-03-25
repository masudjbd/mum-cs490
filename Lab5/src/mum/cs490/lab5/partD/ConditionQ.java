package mum.cs490.lab5.partD;
public interface ConditionQ {

    /** This method causes the current thread to block 
	until it is awakened by the signal method.
     */
    public void await();

    /** If there are threads waiting on 
	then one of the waiters is signaled (awakened)
     */
    public void signal();

    /** If there are threads waiting on this ConditionQ,
	then all of the waiters are signaled
     */
    public void signalAll();

    /** If there are waiters, 
	this method returns true, otherwise it returns false */
    public boolean hasWaiters();

}
