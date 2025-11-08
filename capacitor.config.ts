import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.level4.techmaths4',
  appName: 'TechMaths4',
  webDir: 'www', // required by Capacitor but not used when pointing to a live PWA
  server: {
    url: 'https://level4.technicalmathematics.com', // keep your live PWA link here
    cleartext: false
  }
};

export default config;
