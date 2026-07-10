class AppL10n {
  static const Map<String, String> _es = {
    'app_title': 'Shipping Platform Legacy',
    'quote_form_title': 'Cotizador de Envíos (Dart)',
    'error_generic': 'Ocurrió un error inesperado',
  };

  static String get(String key) => _es[key] ?? key;
}
