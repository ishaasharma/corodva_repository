cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "com.example.sample.plugin.PluginName",
      "file": "plugins/com.example.sample.plugin/www/PluginName.js",
      "pluginId": "com.example.sample.plugin",
      "clobbers": [
        "PluginName"
      ]
    }
  ];
  module.exports.metadata = {
    "com.example.sample.plugin": "0.0.1"
  };
});