module.exports = {
  presets: ['module:@react-native/babel-preset'],
  plugins: [
    [
      'module-resolver',
      {
        alias: {
          '@presentation': './src/presentation',
          '@domain': './src/domain',
          '@application': './src/application',
          '@infrastructure': './src/infrastructure',
          '@feature': './src/feature',
        },
      },
    ],
  ],
};
