import { Controller, Post, Body } from '@nestjs/common';
import { SubscriptionsService } from './subscriptions.service';

@Controller('subscriptions')
export class SubscriptionsController {
  constructor(private readonly subscriptionsService: SubscriptionsService) {}

  @Post('select-plan')
  async selectPlan(@Body() body: any) {
    return this.subscriptionsService.selectPlan(body);
  }

  @Post('verify-payment')
  async verifyPayment(@Body() body: any) {
    return this.subscriptionsService.verifyPayment(body);
  }
}
