class L10n {
  static const Map<String, Map<String, String>> _localizedValues = {
    'en': {
      'success_title': 'Quote Successful!',
      'new_quote': 'New Quote',
      'estimated_time': 'Estimated time: {days} days',
      'shipping_type': 'Shipping type',
      'foreign_zone': 'Foreign Zone',
      'special_handling': 'Special Handling',
      'yes': 'Yes',
      'no': 'No',
    },
    'es': {
      'success_title': '¡Cotización Exitosa!',
      'new_quote': 'Nueva Cotización',
      'estimated_time': 'Tiempo estimado: {days} días',
      'shipping_type': 'Tipo de envío',
      'foreign_zone': 'Zona Foránea',
      'special_handling': 'Manejo Especial',
      'yes': 'Sí',
      'no': 'No',
    },
  };

  static String getString(String key, {String locale = 'es', Map<String, String>? args}) {
    String value = _localizedValues[locale]?[key] ?? key;
    if (args != null) {
      args.forEach((k, v) {
        value = value.replaceAll('{$k}', v);
      });
    }
    return value;
  }
}
