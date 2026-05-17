import {
  WebSocketGateway,
  WebSocketServer,
  SubscribeMessage,
  MessageBody,
  OnGatewayConnection,
  OnGatewayDisconnect,
} from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';

@WebSocketGateway({ cors: { origin: '*' } })
export class EventsGateway implements OnGatewayConnection, OnGatewayDisconnect {
  @WebSocketServer()
  server: Server;

  handleConnection(client: Socket) {
    console.log('⚡ Neural Link Established:', client.id);
  }

  handleDisconnect(client: Socket) {
    console.log('🔌 Neural Link Terminated:', client.id);
  }

  broadcast(event: string, data: any) {
    this.server.emit(event, data);
    console.log(`📢 Broadcasting [${event}]:`, data);
  }

  @SubscribeMessage('NEW_FARMER_QUESTION')
  handleNewFarmerQuestion(@MessageBody() data: any) {
    this.broadcast('NEW_FARMER_QUESTION', data);
  }

  @SubscribeMessage('NEW_EXPERT_REPLY')
  handleNewExpertReply(@MessageBody() data: any) {
    this.broadcast('NEW_EXPERT_REPLY', data);
  }
}
