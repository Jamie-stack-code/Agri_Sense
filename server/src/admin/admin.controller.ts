import { Controller, Get, Post, Body } from '@nestjs/common';
import { AdminService } from './admin.service';

@Controller('admin')
export class AdminController {
  constructor(private readonly adminService: AdminService) {}

  @Get('stats')
  async getStats() {
    return this.adminService.getStats();
  }

  @Get('users')
  async getUsers() {
    return this.adminService.getUsers();
  }

  @Post('provision')
  async provisionUser(@Body() body: any) {
    return this.adminService.provisionUser(body);
  }
}
