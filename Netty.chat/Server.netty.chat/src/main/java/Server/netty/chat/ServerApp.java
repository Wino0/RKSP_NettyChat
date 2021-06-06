package Server.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerApp { // Класс Приложение сервера
    private  static  final int PORT = 8188;
    public static void main(String[] args) {
        EventLoopGroup mainGroup = new NioEventLoopGroup(1); //Создаём пул потоков для подключения клиентов
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //Создаём пул потоков для обработки информации
        try {
            ServerBootstrap sB = new ServerBootstrap();// Инициализация сервера
            sB.group(mainGroup, workerGroup) // Обращаемся к серверу, чтобы использовал 2 пула потоков
                    .channel(NioServerSocketChannel.class) // Канал для подключения клиента к серверу ServerSocket - стандартный канал
                    .childHandler(new ChannelInitializer<SocketChannel>() { // Подключение клиента к серверу, SocketChannel(Информация о соединении клиента ссервером)
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception { // Инициализация клиента после его подключения
                            socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(), new InformationHandler()); // добавляем в конец(addlast) конвеера (pipeline) обработчик
                       }
                    });
            ChannelFuture future = sB.bind(PORT).sync(); // ChannelFuture информация о состоянии сервера. sB.bind связываем Сервер sB с портом 8188 для старта. Sync запуск задачи сервера
            future.channel().closeFuture().sync(); // closeFuture информация об отключении сревера. Когда его закрывают - попадаем в блок finally
        } catch (Exception eX)
        { eX.printStackTrace();}// Отображаем ошибки в коносли
        finally {
                mainGroup.shutdownGracefully(); // .Shutdown.. метод полного отключения
                workerGroup.shutdownGracefully(); // отключаем пул потоков
        }
    }
}
