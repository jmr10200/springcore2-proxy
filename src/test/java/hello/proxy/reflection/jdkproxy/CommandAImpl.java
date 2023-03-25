package hello.proxy.reflection.jdkproxy;

public class CommandAImpl implements CommandA {
    @Override
    public int execute(String command) {
        if ("NORTH".equals(command)) {
            return 0;
        } else if ("SOUTH".equals(command)) {
            return 1;
        } else {
            throw new IllegalArgumentException("Arg must be 'NORTH' or 'SOUTH'");
        }
    }
}
