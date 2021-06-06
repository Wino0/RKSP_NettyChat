package Client.netty.cha;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {
    private SocketChannel channel; // сокет канал
    private callback Message;

    private static  final  String HOST = "localhost";
    private  static  final int PORT = 8188;

    public  Network(callback Message){ // конструктор класса
    this.Message = Message;
        new Thread(() -> { // запуск клиента
            EventLoopGroup workerGroup = new NioEventLoopGroup(); // пул потоков для обработки сетевых событий
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)  // в качестве пула используем - workergroup
                        .channel(NioSocketChannel.class) // в качестве канала - нио сокет канал
                        .handler(new ChannelInitializer<SocketChannel>() { // при подключении открывается сокет канал
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel; // записываем в переменную ссылку на соединение
                                socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(), // входящий и исходящий обработчики
                                        new SimpleChannelInboundHandler<String>(){
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                        if(Message != null){
                                            Message.callback(s);
                                        }
                                    }
                                }
                                );
                            } // при получении строки encoder преобразует её в байтбуфер а декодер наоборот
                        });
                ChannelFuture future = b.connect(HOST,PORT).sync(); // связываем клиента с портом
                future.channel().closeFuture().sync(); // чтобы клиент не закрылся после открытия соединения
            } catch (Exception ex){
                ex.printStackTrace(); // вывод исключений
            } finally {
                workerGroup.shutdownGracefully(); // отключаем пул потока
            }
        }).start();
    }
    public void sendAmassage(String str){
            channel.writeAndFlush(str); // отправляем в канал строку
    }
}
