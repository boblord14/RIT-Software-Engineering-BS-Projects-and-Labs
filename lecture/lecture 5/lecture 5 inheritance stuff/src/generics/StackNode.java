package generics;

/**
 * A stack implementation that uses a Node to represent the structure.
 * @param <T> The type of data the stack will hold
 * @author Sean Strout @ RIT CS
 */
public class StackNode<T> implements Stack<T> {
    /** The top element in the stack (null if empty) */
    private Node<T> top;

    /**
     * Create an empty stack.
     */
    public StackNode() {
        this.top = null;
    }

    @Override
    public boolean empty() { return this.top == null; }

    @Override
    public T pop() {
        assert !empty();
        T element = this.top.getData();
        this.top = this.top.getNext();
        return element;
    }

    @Override
    public void push(T element) {
        this.top = new Node<>(element, this.top);
    }

    @Override
    public T top() {
        assert !empty();
        return this.top.getData();
    }
}
