import { QuestionsService } from './questions.service';
export declare class QuestionsController {
    private readonly questionsService;
    constructor(questionsService: QuestionsService);
    createQuestion(body: any): Promise<any>;
}
