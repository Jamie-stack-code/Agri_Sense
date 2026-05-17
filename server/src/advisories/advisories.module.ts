import { Module } from '@nestjs/common';
import { AdvisoriesController } from './advisories.controller';
import { AdvisoriesService } from './advisories.service';

@Module({
  controllers: [AdvisoriesController],
  providers: [AdvisoriesService],
})
export class AdvisoriesModule {}
