Pod::Spec.new do |s|
  s.name             = 'MobuildEnvuscator'
  s.version          = '1.0.0'
  s.summary          = 'A secure mobile obfuscated configuration framework'
  s.description      = 'Obtains 12 factor configuration to generate obfuscated data, to deobfuscate at runtime.'
  s.homepage         = 'https://github.com/hackelia-micrantha/mobuild-envuscator'
  s.license          = { :type => 'MIT', :file => 'LICENSE' }
  s.author           = { 'Ryan Jennings' => 'ryan.jennings@micrantha.com' }
  s.source           = { :git => 'https://github.com/hackelia-micrantha/mobuild-envuscator.git', :tag => s.version.to_s }
  s.platform         = :ios, '11.0'

  s.pod_target_xcconfig = {
    'PRODUCT_MODULE_NAME' => 'MobuildEnvuscator',
  }
  s.vendored_frameworks = 'MobuildEnvuscator.framework'

end
