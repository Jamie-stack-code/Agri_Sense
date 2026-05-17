import { Controller, Post, Body } from '@nestjs/common';
import { QuestionsService } from './questions.service';

@Controller('questions')
export class QuestionsController {
  constructor(private readonly questionsService: QuestionsService) {}

  @Post()
  async createQuestion(@Body() body: any) {
    return this.questionsService.createQuestion(body);
  }
}
