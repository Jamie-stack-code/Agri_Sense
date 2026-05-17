import { Controller, Post, Body, Headers } from '@nestjs/common';
import { AuthService } from './auth.service';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('verify-token')
  async verifyToken(@Headers('authorization') authHeader: string) {
    const idToken = authHeader?.split('Bearer ')[1] || '';
    return this.authService.verifyTokenAndGetProfile(idToken);
  }

  // --- MOCK ENDPOINTS FOR TRANSITION ---
  // Since we switched to Firebase Auth, the client (Android) should use the Firebase SDK
  // to get an OTP and verify it. It should then send the ID Token to `/verify-token`.
  // These endpoints are here temporarily so the app doesn't crash if it calls them.

  @Post('phone-signup')
  async phoneSignup(@Body() body: any) {
    console.warn('phone-signup called - Client should use Firebase SDK directly');
    return { message: 'OTP sent to your phone (Mock)', otp: '123456' };
  }

  @Post('verify-phone-otp')
  async verifyPhoneOtp(@Body() body: any) {
    console.warn('verify-phone-otp called - Client should use Firebase SDK directly');
    return this.authService.verifyTokenAndGetProfile('', body.phone);
  }

  @Post('phone-signin')
  async phoneSignin(@Body() body: any) {
    console.warn('phone-signin called - Client should use Firebase SDK directly');
    return this.authService.verifyTokenAndGetProfile('', body.phone);
  }

  @Post('update-profile')
  async updateProfile(@Body() body: any) {
    // Assuming the client passes the phone or uid for now
    const uid = body.uid || ('mock-uid-' + body.phone);
    return this.authService.updateProfile(uid, body);
  }
}
