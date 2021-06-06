package Server.netty.chat;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.ArrayList;
import java.util.List;

public class InformationHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channels = new ArrayList<>(); // список ссылок на каналы клиентов для рассылки данных всем клиентам
    private  static  int clientIndex = 1;
    private String clientName;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // Открыли соединение для клиента
        System.out.println("Клиент подключился " + ctx); // информация о подключении
        channels.add(ctx.channel()); // при подключении добавляем в список
        clientName = "Клиент № " +  clientIndex + " ";
        clientIndex++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
    System.out.println("Получено сообщение:" + s);
    String out = String.format("[%s]: %s\n", clientName, s);// формат вывода сообщений для всех полключенных клиентов
    for (Channel ch : channels){
        ch.writeAndFlush(out); // о=тправка
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // Блок исключений при работе с клиентом(ошибки)
        cause.printStackTrace();
        ctx.close();// выход
    }

    public static void main(String[] args) {

    }
}
