import 'package:flutter_riverpod/flutter_riverpod.dart';

class FormValuesState {
  final Map<String, dynamic> values;
  final Map<String, String?> errors;

  FormValuesState({
    this.values = const {},
    this.errors = const {},
  });

  FormValuesState copyWith({
    Map<String, dynamic>? values,
    Map<String, String?>? errors,
  }) {
    return FormValuesState(
      values: values ?? this.values,
      errors: errors ?? this.errors,
    );
  }
}

class FormStateNotifier extends StateNotifier<FormValuesState> {
  FormStateNotifier() : super(FormValuesState());

  void updateValue(String id, dynamic value) {
    state = state.copyWith(
      values: {...state.values, id: value},
      errors: {...state.errors, id: null}, // Clear error on change
    );
  }

  void setError(String id, String? message) {
    state = state.copyWith(
      errors: {...state.errors, id: message},
    );
  }

  void clearErrors() {
    state = state.copyWith(errors: {});
  }

  void reset() {
    state = FormValuesState();
  }
}

final sduiFormStateProvider =
    StateNotifierProvider<FormStateNotifier, FormValuesState>((ref) {
  return FormStateNotifier();
});
